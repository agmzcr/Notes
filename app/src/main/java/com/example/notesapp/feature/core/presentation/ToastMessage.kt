package com.example.notesapp.feature.core.presentation

import android.content.Context
import android.widget.Toast

fun toastMessage(
    context: Context,
    message: String
) = Toast.makeText(
    context, message, Toast.LENGTH_SHORT
).show()