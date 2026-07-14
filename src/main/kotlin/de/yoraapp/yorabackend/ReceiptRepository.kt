package de.yoraapp.yorabackend

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ReceiptRepository : CrudRepository<Receipt, UUID>