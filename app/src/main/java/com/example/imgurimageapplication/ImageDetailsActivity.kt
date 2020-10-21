package com.example.imgurimageapplication

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.imgurimageapplication.database.DatabaseClient
import com.example.imgurimageapplication.model.CommentModel
import com.example.imgurimageapplication.util.Constants
import kotlin.concurrent.thread

class ImageDetailsActivity : AppCompatActivity() {
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        val image: ImageView = findViewById(R.id.imageViewDetails)
        editText = findViewById(R.id.editTextComment)
        val button: Button = findViewById(R.id.button)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val url: String = intent.extras?.getString(Constants.IMAGE_URL) ?: ""
        val position: Int = intent.extras?.getInt(Constants.IMAGE_POSITION) ?: 0
        val imageTitle: String = intent.extras?.getString(Constants.IMAGE_TITLE) ?:
        getString(R.string.no_title_string)

        toolbar.title = imageTitle
        setSupportActionBar(toolbar)
        toolbar.setTitleTextAppearance(this, R.style.ToolbarStyle)
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val commentModel = CommentModel()
        val database = DatabaseClient.getDataBaseInstance(applicationContext)

        val uri = Uri.parse(url)
        Glide.with(this).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image)

        if (database.appDatabase.comments.all != null) {
            if (position == database.appDatabase.comments.all.position) {
                editText?.setText(database.appDatabase.comments.all.getComment())
            }
        }

        button.setOnClickListener { view ->
            val commentString = editText?.text.toString()
            if(commentString.isNotEmpty()) {
                commentModel.setComment(commentString)
                commentModel.setPosition(position)
                thread(isDaemon = true) { database.appDatabase.comments.insert(commentModel) }
                Toast.makeText(this, "Comment stored successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter comment first", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            editText?.setText("");
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        editText?.setText("");
        finish()
    }

}
