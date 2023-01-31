package com.iiplabs.rp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class RpApplication

fun main(args: Array<String>) {
	runApplication<RpApplication>(*args)
}
