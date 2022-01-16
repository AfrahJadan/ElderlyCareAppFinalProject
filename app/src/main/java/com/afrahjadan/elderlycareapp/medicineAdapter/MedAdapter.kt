package com.afrahjadan.elderlycareapp.medicineAdapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afrahjadan.elderlycareapp.R
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.fragment.ViewMedicineFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class MedAdapter(private val medList: MutableList<MedicineItem?>) :
    RecyclerView.Adapter<MedAdapter.MedViewHolder>() {
    private lateinit var id: String
    private val db = FirebaseFirestore.getInstance()
    class MedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medShow: TextView = itemView.findViewById(R.id.medShow)
        val dosMed: TextView = itemView.findViewById(R.id.dozMed)
        val medTime: TextView = itemView.findViewById(R.id.timeMed)
        val medDate: TextView = itemView.findViewById(R.id.dateMed)
        val medEdit: ImageButton = itemView.findViewById(R.id.editMed)
        val medDelete: ImageButton = itemView.findViewById(R.id.deleteMed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        val medAdapterLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.med_item_list,
            parent, false
        )
        return MedViewHolder(medAdapterLayout)
    }

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        val medItem = medList[position]
        id = medItem!!.id
        holder.medShow.text = medItem?.medType.toString()
        holder.dosMed.text = medItem?.dose.toString()
        holder.medTime.text = medItem?.medTime.toString()
        holder.medDate.text = medItem?.medDate.toString()
        holder.medEdit.setOnClickListener {

            val action =
                ViewMedicineFragmentDirections.actionViewMedicineFragmentToEditMedicineFragment(
                    medItem!!.id
                )
            holder.itemView.findNavController()
                .navigate(action)
        }
        holder.medDelete.setOnClickListener {
            deleteMad()
        }
    }

    override fun getItemCount(): Int {
        return medList.size
    }

    private fun deleteMad() {
        db.collection("Medicines").document(id).delete()
    }
}