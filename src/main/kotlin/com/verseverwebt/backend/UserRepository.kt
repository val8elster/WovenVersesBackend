<<<<<<<< HEAD:src/main/kotlin/com/hsbsose24/vvbackend/UserRepository.kt
package com.hsbsose24.vvbackend
========
package com.verseverwebt.backend
>>>>>>>> main:src/main/kotlin/com/verseverwebt/backend/UserRepository.kt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByName(name: String): Boolean
    fun existsByEmail(email: String): Boolean
}
