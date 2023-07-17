package com.gidskql6671.today_i_baekjoon.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User Not Found")
class UserNotFoundException: RuntimeException()