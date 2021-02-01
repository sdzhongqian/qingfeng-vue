import request from '@/utils/request'
import store from '@/store'
/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
export function login (parameter) {
  return request({
    url: '/system/login/login',
    method: 'post',
    data: parameter,
  })
}


export function getInfo () {
  return request({
    url: '/system/user/findUserInfo',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function logout () {
  return request({
    url: '/system/login/outLogin',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function getLoginUser () {
  return request({
    url: '/system/user/findLoginUser',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

export function updateUser (parameter) {
  return request({
    url: '/system/user/updateUser',
    method: 'post',
    data: parameter,
  })
}

export function updatePwd (parameter) {
  return request({
    url: '/system/user/updateMyPwd',
    method: 'post',
    data: parameter,
  })
}

export function updateSwitchOrganize (parameter) {
  return request({
    url: '/system/user/updateSwitchOrganize',
    method: 'post',
    data: parameter,
  })
}

