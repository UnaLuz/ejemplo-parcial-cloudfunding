package com.example

class NoProjectNameException : IllegalArgumentException()

class NegativeAmountException(message: String? = null) : IllegalArgumentException(message)

class NoResponsablesException: IllegalArgumentException()

class IllegalDateException: IllegalArgumentException()
