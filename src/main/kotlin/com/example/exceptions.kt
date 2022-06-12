package com.example

class NoProjectNameException : IllegalArgumentException()

class MoneyValueTooLowException(message: String? = null) : IllegalArgumentException(message)

class NoResponsablesException: IllegalArgumentException()

class NoInvestmentsException(message: String? = null): UnsupportedOperationException(message)

class IllegalDateException(message: String? = null): IllegalArgumentException(message)

class NotEnoughProjectsException(message: String?): UnsupportedOperationException(message)

class DonationException(message: String?): RuntimeException(message)