package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController @Autowired
    constructor(private val userRepository: UserRepository) {

    @GetMapping
    fun getAllUsers(): List<User> = userRepository.findAll()

    @PostMapping
    fun createUser(@RequestBody user: User): User = userRepository.save(user)

    @GetMapping("/rankings")
    fun getUsersByRank(): List<User> = userRepository.findAll().sortedBy { it.rank }

    @PutMapping("/calculate-rankings")
    fun calculateRankings(): List<User> {
        val users = userRepository.findAll()
        users.sortedBy { it.time1 + it.time2 + it.time3 + it.time4 + it.time5 }.forEachIndexed { index, user ->
            user.rank = index + 1
        }
        userRepository.saveAll(users)
        return users
    }
}
