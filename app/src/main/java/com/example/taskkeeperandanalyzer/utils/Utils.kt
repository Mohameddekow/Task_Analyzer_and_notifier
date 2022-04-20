package com.example.taskkeeperandanalyzer.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.taskkeeperandanalyzer.R


fun loadImageWithGlide(url: String, context: Context, view: ImageView){
    val option = RequestOptions().placeholder(R.drawable.profile)
        .error(R.drawable.profile)
    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(url)
        .into(view)
}

fun showShortToast(context: Context, message: String){
    Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
}
fun showLongToast(context: Context, message: String){
    Toast.makeText(context, message,Toast.LENGTH_LONG).show()
}


fun showProgressDialog(context: Context, title: String, message: String): ProgressDialog {
    val progressDialog = ProgressDialog(context)
    progressDialog.setTitle(title)
    progressDialog.setMessage(message)
    progressDialog.setCanceledOnTouchOutside(false)
    return progressDialog
}



//alert dialog for password reset
fun showResetPasswordAlertDialog(context: Context){
    val builder = AlertDialog.Builder(context).create()

    val view = LayoutInflater.from(context).inflate(R.layout.reset_password_layout, null)

    builder.setView(view)


    val  goToGmailBtn = view.findViewById<Button>(R.id.goToGmailBtn)
    //take me to gmail on clicking the btn
    goToGmailBtn.setOnClickListener {

        //open the email app
        val intent = Intent(Intent.ACTION_MAIN)
        //the app will be open in a new task and onBackPressed you will come back to the app
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        try {
            startActivity(context,intent, null)
        } catch (e: Exception) {
            AlertDialog.Builder(context)
                .setTitle("Email App Not Found")
                .show()
        }
        builder.dismiss()
    }


    //dismiss the  on clicking the dismiss btn
    val dismissBtn = view.findViewById<Button>(R.id.dismissBtn)
    dismissBtn.setOnClickListener {
        builder.dismiss()
    }

    builder.setCanceledOnTouchOutside(false)
    builder.show()
}



//alert dialog for login registration
fun showAlertDialog(context: Context){

    val builder = AlertDialog.Builder(context).create()

    val view = LayoutInflater.from(context).inflate(R.layout.email_link_alert_dialog_layout, null)

    builder.setView(view)


    val  goToGmailBtn = view.findViewById<Button>(R.id.goToGmailBtn)
    //take me to gmail on clicking the btn
    goToGmailBtn.setOnClickListener {

        //open the email app
        val intent = Intent(Intent.ACTION_MAIN)
        //the app will be open in a new task and onBackPressed you will come back to the app
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        try {
          startActivity(context,intent, null)
        } catch (e: Exception) {
            AlertDialog.Builder(context)
                .setTitle("Email App Not Found")
                .show()
        }
        builder.dismiss()
    }


    val  gmailIcon = view.findViewById<ImageView>(R.id.gmailIcon)
    //take me to gmail on clicking the gmail icon
    gmailIcon.setOnClickListener {

        //open the email app
        val intent = Intent(Intent.ACTION_MAIN)
        //the app will be open in a new task and onBackPressed you will come back to the app
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        try {
            startActivity(context, intent, null)
        } catch (e: Exception) {
            AlertDialog.Builder(context)
                .setTitle("Email App Not Found")
                .show()
        }

        builder.dismiss()
    }


    //dismiss the  on clicking the dismiss btn
    val dismissBtn = view.findViewById<Button>(R.id.dismissBtn)
    dismissBtn.setOnClickListener {
        builder.dismiss()
    }


    builder.setCanceledOnTouchOutside(false)
    builder.show()
}