import request from '@/utils/request'
export function geiListPage (parameter) {
  return request({
    url: '/system/user/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  return request({
    url: '/system/user/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  return request({
    url: '/system/user/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/user/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function getList (parameter) {
  return request({
    url: '/system/user/findList',
    method: 'post',
    data: parameter
  })
}

export function updatePwd (parameter) {
  return request({
    url: '/system/user/updatePwd',
    method: 'post',
    data: parameter
  })
}


export function getMyOrganizeListPage (parameter) {
  return request({
    url: '/system/user/findMyOrganizeListPage',
    method: 'post',
    data: parameter
  })
}

export function saveOrUpdateUserOrganize (parameter) {
  return request({
    url: '/system/user/saveOrUpdateUserOrganize',
    method: 'post',
    data: parameter
  })
}


export function delUserOrganize (id) {
  return request({
    url: `/system/user/delUserOrganize?id=${id}`,
    // url: '/system/user/delUserOrganize?id='+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function getRoleAuth (parameter) {
  return request({
    url: '/system/user/findRoleAuth',
    method: 'post',
    data: parameter
  })
}

export function getOrganizeAuth (parameter) {
  return request({
    url: '/system/user/findOrganizeAuth',
    method: 'post',
    data: parameter
  })
}

export function updateAuth (parameter) {
  return request({
    url: '/system/user/updateAuth',
    method: 'post',
    data: parameter
  })
}

export function getUserInfo (id) {
  return request({
    url: `/system/user/getUserInfo?id=${id}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}