package com.afrahjadan.elderlycareapp.medicineAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afrahjadan.elderlycareapp.data.MedicineItem
import com.afrahjadan.elderlycareapp.databinding.MedItemListBinding
import com.afrahjadan.elderlycareapp.fragment.ViewMedicineFragmentDirections


class MedAdapter : ListAdapter<MedicineItem, MedAdapter.MedItemViewHolder>(MedItemViewHolder) {

    class MedItemViewHolder(var binding: MedItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemMed: MedicineItem) {
            binding.medShow.text = itemMed.medType
            binding.dozMed.text = itemMed.dose
            binding.timeMed.text = itemMed.medTime
            binding.dateMed.text = itemMed.medDate
        }

        companion object DiffCallback : DiffUtil.ItemCallback<MedicineItem>() {
            override fun areItemsTheSame(oldItem: MedicineItem, newItem: MedicineItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: MedicineItem, newItem: MedicineItem): Boolean {
                return oldItem.userId == newItem.userId
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedItemViewHolder {
        return MedItemViewHolder(
            MedItemListBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MedItemViewHolder, position: Int) {
        val medicinesList = getItem(position)
        holder.binding.editMed.setOnClickListener {

            val action =
                ViewMedicineFragmentDirections.actionViewMedicineFragmentToEditMedicineFragment(
                    medicinesList.userId
                )

            holder.itemView.findNavController().navigate(action)

        }
        holder.binding.deleteMed.setOnClickListener {

            val action =
                ViewMedicineFragmentDirections.actionViewMedicineFragmentSelf(medicinesList.dose)
                 holder.itemView.findNavController().navigate(action)


        }
        holder.bind(medicinesList)
    }

}




