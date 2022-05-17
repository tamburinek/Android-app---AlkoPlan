package com.example.alkoplan

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.alkoplan.data.event.Event
import com.example.alkoplan.data.user.UserDatabase
import com.example.alkoplan.databinding.EventItemBinding

class EventAdapter: RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    lateinit var db: UserDatabase
    var eventList = ArrayList<Event>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        holder.bind(eventList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, EventDetail::class.java)
            intent.putExtra("Title", eventList[position].title)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = EventItemBinding.bind(itemView)
        @SuppressLint("SetTextI18n")
        fun bind(event: Event) = with(binding){
            val placeInfo = event.place
            val timeInfo = String.format("%02d:%02d", event.hour, event.minute-1);
            when (placeInfo) {
                "Sasazu" -> {
                    eventImage.setBackgroundResource(R.drawable.sasazu_restaurant)
                }
                "Bar21" -> eventImage.setBackgroundResource(R.drawable.bar21)
                "Nadrazka" -> eventImage.setBackgroundResource(R.drawable.nadrazka)
                else -> eventImage.setBackgroundResource(R.drawable.default_event)
            }
            eventInfo.text = event.descriptionEvent
            eventTitle.text = event.title
            eventTime.text = event.day.toString() +
                    "." + event.month.toString() + "." +
                    event.year.toString() + " - " + timeInfo

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addEvent(event: Event) {
        eventList.add(event)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addEventList(eventsList: LiveData<List<Event>>) {
        eventsList.observeForever {
            eventList = it as ArrayList<Event>
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteEvent(event: Event){
        eventList.remove(event)
        notifyDataSetChanged()
    }

}