package br.edu.ifsp.ads.pdm.mycontacts.model.database

import br.edu.ifsp.ads.pdm.mycontacts.model.dao.ContactDao
import br.edu.ifsp.ads.pdm.mycontacts.model.entity.Contact
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class ContactDaoFirebase: ContactDao {
    private val CONTACT_DATABASE_ROOT_NODE = "contacts"
    private val contactRealtimeDatabase = Firebase.database.getReference(CONTACT_DATABASE_ROOT_NODE)

    // Simula um consulta ao banco de dados
    private val contactList = mutableListOf<Contact>()
    init{
        contactRealtimeDatabase.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Contact? = snapshot.getValue<Contact>()

                contact?.let{ _contact ->
                    if (!contactList.any { _contact.id == it.id}) {
                        contactList.add(_contact)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Contact? = snapshot.getValue<Contact>()

                contact?.let{ _contact ->
                    val position = contactList.indexOfFirst { _contact.id == it.id }
                    if (position != -1) {
                        contactList[position] = _contact
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val contact: Contact? = snapshot.getValue<Contact>()
                contactList.remove(contact)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
    }

    private fun generateId(): Int {
        val random = Random(System.currentTimeMillis())
        var id: Int
        do{
            id = random.nextInt()
        } while(contactList.any{ it.id == id})
        return id
    }

    override fun createContact(contact: Contact): Int {
        contact.id = generateId()
        contactRealtimeDatabase.child(contact.id.toString()).setValue(contact)
        return contact.id!!
    }

    override fun retrieveContact(id: Int): Contact? {
        val position = contactList.indexOfFirst { it.id == id }
        return contactList[position]
    }

    override fun retrieveContacts(): MutableList<Contact> = contactList

    override fun updateContact(contact: Contact): Int {
        contactRealtimeDatabase.child(contact.id.toString()).setValue(contact)
        return 1
    }

    override fun deleteContact(id: Int): Int {
        contactRealtimeDatabase.child(id.toString()).removeValue()
        return 1
    }
}