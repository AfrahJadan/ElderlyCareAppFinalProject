package com.afrahjadan.elderlycareapp.appoitmentAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afrahjadan.elderlycareapp.data.AppointmentItem
import com.afrahjadan.elderlycareapp.databinding.AppItemListBinding
import com.afrahjadan.elderlycareapp.fragment.ViewAppointmentFragmentDirections


class AppAdapter : ListAdapter<AppointmentItem, AppAdapter.ItemViewHolder>(ItemViewHolder.DiffCallback) {

    class ItemViewHolder(var binding: AppItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemApp: AppointmentItem) {

            binding.dateAppTv.text = itemApp.appointmentDate
            binding.timeAppTv.text = itemApp.appointmentTime
            binding.appResTv.text = itemApp.appointmentReason
            binding.hosNameTv.text = itemApp.hospitalName
        }

        companion object DiffCallback : DiffUtil.ItemCallback<AppointmentItem>() {
            override fun areItemsTheSame(
                oldItem: AppointmentItem,
                newItem: AppointmentItem
            ): Boolean {
                return oldItem.appUserId == newItem.appUserId
            }

            override fun areContentsTheSame(
                oldItem: AppointmentItem,
                newItem: AppointmentItem
            ): Boolean {
                return oldItem.appUserId == newItem.appUserId
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            AppItemListBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val appointmentList = getItem(position)
        holder.binding.editApp.setOnClickListener {

            val action =
                ViewAppointmentFragmentDirections.actionViewAppointmentFragmentToEditAppointmentFragment(
                    appointmentList.appUserId
                )

            holder.itemView.findNavController().navigate(action)

        }
        holder.bind(appointmentList)
    }

//class AppAdapter(private val appList: MutableList<AppointmentItem?>) :
//    RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

//    private val db = FirebaseFirestore.getInstance()
//
//    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val appTime: TextView = itemView.findViewById(R.id.timeAppTv)
//        val appDate: TextView = itemView.findViewById(R.id.dateAppTv)
//        val appRes: TextView = itemView.findViewById(R.id.appResTv)
//        val hosName: TextView = itemView.findViewById(R.id.hosNameTv)
//        val appEdit: ImageButton = itemView.findViewById(R.id.editApp)
//        val appDelete: ImageButton = itemView.findViewById(R.id.deleteApp)
//        val cardMed: CardView = itemView.findViewById(R.id.app_card)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
//        val appAdapterLayout =
//            LayoutInflater.from(parent.context).inflate(R.layout.app_item_list, parent, false)
//        return AppViewHolder(appAdapterLayout)
//    }
//
//    @SuppressLint("ResourceAsColor")
//    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
//        val appItem = appList[position]
//        id = appItem!!.appUserId
//        holder.appTime.text = appItem.appointmentTime
//        holder.appDate.text = appItem.appointmentDate
//        holder.appRes.text = appItem.appointmentReason
//        holder.hosName.text = appItem.hospitalName
//
////isPassed(appItem?.appointmentDate!!).toString()
////        if (isPassed(appItem.appointmentDate!!)) {
////
////            holder.cardMed.setBackgroundColor(R.color.baby_blue)
////        }
////else{
////    holder.cardMed.setBackgroundColor(R.color.purple_200)
////}
//        holder.appEdit.setOnClickListener {
//            val action =
//                ViewAppointmentFragmentDirections.actionViewAppointmentFragmentToEditAppointmentFragment(
//                    appItem.appUserId
//                )
//            holder.itemView.findNavController()
//                .navigate(action)
//        }
//        holder.appDelete.setOnClickListener {
//            deleteApp()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return appList.size
//    }
//
//    private fun deleteApp() {
//        db.collection("Appointment").document(id).delete()
//
//    }

//    private fun isPassed(appDate: String): Boolean {
//        val dateAppPass = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(appDate)
//        return dateAppPass.before(Date())
//    }

}


