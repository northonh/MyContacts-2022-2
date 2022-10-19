package br.edu.ifsp.ads.pdm.mycontacts.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.ads.pdm.mycontacts.R
import br.edu.ifsp.ads.pdm.mycontacts.databinding.TileContactBinding
import br.edu.ifsp.ads.pdm.mycontacts.model.Contact

class ContactAdapter(
    context: Context,
    private val contactList: MutableList<Contact>
) : ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {
    private data class TileContactHolder(val nameTv: TextView, val emailTv: TextView)
    private lateinit var tcb: TileContactBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]
        var contactTileView = convertView
        if (contactTileView == null) {
            // Inflo uma nova célula
            tcb = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )

            contactTileView = tcb.root
            val tileContactHolder = TileContactHolder(tcb.nameTv, tcb.emailTv)
            contactTileView.tag = tileContactHolder
        }
        with (contactTileView.tag as TileContactHolder) {
            nameTv.text = contact.name
            emailTv.text = contact.email
        }

        return contactTileView
    }
}