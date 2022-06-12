package com.example

interface MailSender {
    fun enviar(mail: Mail)
}

data class Mail(val from: String, val to: String, val subject: String, val content: String)
