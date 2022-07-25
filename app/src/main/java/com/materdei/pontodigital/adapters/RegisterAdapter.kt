package com.materdei.pontodigital.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materdei.pontodigital.R
import com.materdei.pontodigital.dto.Authentication
import com.materdei.pontodigital.dto.RegisterDetails

/* TODO 004.16: recycler view que gerencia a apresentação dos RegisterDetails, principalmente
*   dos workdays */
class RegisterAdapter (private var registers: MutableList<RegisterDetails>, private var user: Authentication) :
    RecyclerView.Adapter<RegisterAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.register_item,parent,false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(registers[position])
    }

    override fun getItemCount(): Int = registers.size


    inner class DataViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val collapsedInfoImg: ImageView
            by lazy { itemView.findViewById(R.id.collapsedInfoImg) }

        private val punchRecycleView: RecyclerView
            by lazy { itemView.findViewById(R.id.punchRecycleView) }

        private val collapsedBtn: ConstraintLayout
            by lazy { itemView.findViewById(R.id.collapsedBtn) }

        private val dateTextView: TextView
            by lazy { itemView.findViewById(R.id.dateTextView) }

        private val workedHourTextView: TextView
            by lazy { itemView.findViewById(R.id.workedHourTextView) }

        init {
            collapsedBtn.setOnClickListener {
                if (punchRecycleView.isVisible) {
                    punchRecycleView.visibility = View.GONE
                    collapsedInfoImg.setImageResource(R.drawable.ic_is_not_collapsed)
                } else {
                    punchRecycleView.visibility = View.VISIBLE
                    collapsedInfoImg.setImageResource(R.drawable.ic_is_collapsed)
                }
            }
        }

        fun bind(data: RegisterDetails){
            with(data.workday){
                dateTextView.text = date
                workedHourTextView.text = hours
            }

            /* Configuração do Punches Recycler View */
            punchRecycleView.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                adapter = PunchAdapter(data.punches, user)
            }
        }

    }

    fun updateRegisters(newRegisters: MutableList<RegisterDetails>) {
        registers = newRegisters
        notifyDataSetChanged()
    }

    fun updateUser(newUser: Authentication) {
        user = newUser
        notifyDataSetChanged()
    }
}