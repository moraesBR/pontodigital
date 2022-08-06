package com.materdei.pontodigital.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.materdei.pontodigital.R
import com.materdei.pontodigital.di.Authentication
import com.materdei.pontodigital.domain.model.DataModel.Punch
import com.materdei.pontodigital.utils.Constants.Companion.PUNCHADAPTER_USERNAME_EMPTY
import com.materdei.pontodigital.utils.PunchCard


/* TODO 004.17: recycler view que gerencia a apresentação dos punches */
class PunchAdapter(private val punches: List<Punch>, private val user: Authentication) : RecyclerView.Adapter<PunchAdapter.PunchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PunchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.punch_item,parent,false)
        return PunchViewHolder(view)
    }

    override fun onBindViewHolder(holder: PunchViewHolder, position: Int) {
        holder.bind(punches[position])
    }

    override fun getItemCount(): Int = punches.size


    inner class PunchViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private val punchLabel: LinearLayout by lazy { itemView.findViewById(R.id.punchLabel) }
        private val nameWorker: TextView by lazy { itemView.findViewById(R.id.nameWorker) }
        private val dateInfo: TextView by lazy { itemView.findViewById(R.id.dateInfo) }
        private val timeInfo: TextView by lazy { itemView.findViewById(R.id.timeInfo) }
        private val punchImage: ImageView by lazy { itemView.findViewById(R.id.punchImage) }

        fun bind(data: Punch){
            nameWorker.text = if (user.name.isNotEmpty()) user.name else PUNCHADAPTER_USERNAME_EMPTY
            with(data){
                dateInfo.text = date
                timeInfo.text = time
                selectPunchType(PunchCard.getPunchCardTypeByName(punch.uppercase())!!)
            }
        }

        private fun selectPunchType(punch: PunchCard){
            when(punch){
                PunchCard.IN -> {
                    punchLabel.setBackgroundResource(R.color.blue)
                    punchImage.setImageResource(R.drawable.gowork)
                }
                PunchCard.OUT -> {
                    punchLabel.setBackgroundResource(R.color.red)
                    punchImage.setImageResource(R.drawable.gohome)
                }
            }
        }
    }
}

