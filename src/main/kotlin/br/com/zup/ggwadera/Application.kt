package br.com.zup.ggwadera

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zup.ggwadera")
		.start()
}

