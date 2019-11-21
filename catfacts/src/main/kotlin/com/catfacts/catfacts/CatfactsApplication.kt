package com.catfacts.catfacts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatfactsApplication

fun main(args: Array<String>) {
	runApplication<CatfactsApplication>(*args)
}
