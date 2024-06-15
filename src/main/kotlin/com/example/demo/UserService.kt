package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class UserService(@Autowired val userRepository: UserRepository) {

    fun getWhereValidRank(): List<User> {
        var users = ArrayList<User>()
        val allUsers = userRepository.findAll()

        for(user in allUsers){
            checkValidRank(user.id);
            if(user.rank != 0){
                users.add(user)
            }
        }
        return users
    }

    fun checkValidRank(id: Long): Unit {
        val userO = userRepository.findById(id)
        userO.ifPresent { user ->
            if(user.time7 == 0f){
                user.rank = 0
            }
            else {
                user.rank = 10
            }
            userRepository.save(user)
        }
    }

    fun getWhereExistingRank(): List<User> {
        var users = ArrayList<User>()
        val allUsers = userRepository.findAll()

        for(user in allUsers){
            if(user.rank != 0){
                users.add(user)
            }
        }
        return users
    }
}
