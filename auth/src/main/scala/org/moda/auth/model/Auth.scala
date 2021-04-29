package org.moda.auth.model

object Auth {

  //用户登陆token包装类
  final case class UserAuthToken(
                                  userId: Long
                                  , code: Option[String] = None
                                  // , uuid: String
                                  // , openId: String
                                  // , unionId: String
                                  // , name: String
                                )

  //授权地址
  final case class AuthUrlResult(authUrl: String, loginExpire: Long)

}
