package de.yoraapp.yorabackend.message

import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, String>