package br.edu.ifsp.ads.pdm.mycontacts.controller

import android.os.AsyncTask
import androidx.room.Room
import br.edu.ifsp.ads.pdm.mycontacts.model.entity.Contact
import br.edu.ifsp.ads.pdm.mycontacts.model.dao.ContactDao
import br.edu.ifsp.ads.pdm.mycontacts.model.dao.ContactRoomDao
import br.edu.ifsp.ads.pdm.mycontacts.model.dao.ContactRoomDao.Constant.CONTACT_DATABASE_FILE
import br.edu.ifsp.ads.pdm.mycontacts.model.database.ContactDaoSqlite
import br.edu.ifsp.ads.pdm.mycontacts.model.database.ContactRoomDaoDatabase
import br.edu.ifsp.ads.pdm.mycontacts.view.MainActivity

class ContactRoomController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: ContactRoomDao by lazy {
        Room.databaseBuilder(
            mainActivity,
            ContactRoomDaoDatabase::class.java,
            CONTACT_DATABASE_FILE
        ).build().getContactRoomDao()
    }

    fun insertContact(contact: Contact) {
        Thread {
            contactDaoImpl.createContact(contact)
            getContacts()
        }.start()
    }

    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() {
        object: AsyncTask<Unit, Unit, MutableList<Contact>>(){
            override fun doInBackground(vararg params: Unit?): MutableList<Contact> {
                val returnList = mutableListOf<Contact>()
                returnList.addAll(contactDaoImpl.retrieveContacts())
                return returnList
            }

            override fun onPostExecute(result: MutableList<Contact>?) {
                super.onPostExecute(result)
                if (result != null){
                    mainActivity.updateContactList(result)
                }
            }
        }.execute()
    }

    fun editContact(contact: Contact) {
        Thread {
            contactDaoImpl.updateContact(contact)
            getContacts()
        }.start()
    }

    fun removeContact(contact: Contact) {
        Thread {
            contactDaoImpl.deleteContact(contact)
            getContacts()
        }.start()
    }
}
