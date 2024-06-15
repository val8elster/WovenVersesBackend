package com.hsbsose24.vvbackend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController @Autowired
    constructor(
        private val userRepository: UserRepository,
        private val userService: UserService
    )
    {
    @GetMapping
    fun getAllUsers(): List<User> = userRepository.findAll()

    @PostMapping
    fun createUser(@RequestBody user: User): User = userRepository.save(user)

    @GetMapping("/rankings")
    fun getUsersByRank(): List<User> {
        calculateRankings()
        return userService.getWhereExistingRank().sortedBy { it.rank }
    }

    @PutMapping("/calculate-rankings")
    fun calculateRankings(): Unit {
        val users = userService.getWhereValidRank()
        users.sortedBy { it.time1 + it.time2 + it.time3 + it.time4 + it.time5 }.forEachIndexed { index, user ->
            user.rank = index + 1
        }
        userRepository.saveAll(users)
    }

    @PutMapping("/{id}/chapter/{chapter}/time")
    fun updateChapterTime(@PathVariable id: Long, @PathVariable chapter: Int, @RequestBody time: Float): Unit {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        when (chapter) {
            1 -> user.time1 = time
            2 -> user.time2 = time
            3 -> user.time3 = time
            4 -> user.time4 = time
            5 -> user.time5 = time
            6 -> user.time6 = time
            7 -> user.time7 = time
            else -> throw IllegalArgumentException("Invalid chapter number")
        }
        userRepository.save(user)
    }

    @GetMapping("/{id}/chapter/{chapter}/time")
    fun getChapterTime(@PathVariable id: Long, @PathVariable chapter: Int): Float {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        return when (chapter) {
            1 -> user.time1
            2 -> user.time2
            3 -> user.time3
            4 -> user.time4
            5 -> user.time5
            6 -> user.time6
            7 -> user.time7
            else -> throw IllegalArgumentException("Invalid chapter number")
        }
    }
    @PutMapping("/{id}/intro")
    fun updateIntroCompleted(@PathVariable id: Long): Unit {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        user.introCompleted = true
        userRepository.save(user)
    }

    @GetMapping("/{id}/intro")
    fun getIntroCompleted(@PathVariable id: Long): Boolean {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        return user.introCompleted
    }
}
