package com.example.arctest.main

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.arctest.R
import com.example.arctest.common.BASE_URL
import com.example.arctest.detail.WebActivity
import com.example.arctest.model.Poster

class PosterListAdapter(val activity: FragmentActivity, val posterList: List<Poster>): Adapter<PosterListAdapter.PosterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_main_activity, parent, false)
        return PosterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val poster = posterList[position]
        Glide.with(activity).load(BASE_URL + poster.frontmatter.banner.childImageSharp.fixed.src).into(holder.poster)

        if (poster.frontmatter.title.isNullOrEmpty()){
            holder.title.visibility = View.GONE
        }else{
            holder.title.visibility = View.VISIBLE
            holder.title.text = poster.frontmatter.title
        }

        val subTitle = poster.frontmatter.date
        if (subTitle.isNullOrEmpty()){
            holder.subTitle.visibility = View.GONE
        }else{
            holder.subTitle.visibility = View.VISIBLE
            holder.subTitle.text = subTitle
        }

        holder.itemView.setOnClickListener {
            if (poster.frontmatter.path.isNullOrEmpty()){
                Toast.makeText(activity, "path is invalid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            WebActivity.start(activity, BASE_URL + poster.frontmatter.path)
        }
    }

    override fun getItemCount(): Int {
        return posterList.size
    }

    class PosterViewHolder(itemView: View): ViewHolder(itemView) {
        val poster: ImageView by lazy { itemView.findViewById(R.id.poster) }
        val title: TextView by lazy { itemView.findViewById(R.id.title) }
        val subTitle: TextView by lazy { itemView.findViewById(R.id.sub_title) }
    }
}