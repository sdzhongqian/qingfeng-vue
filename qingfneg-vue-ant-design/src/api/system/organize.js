import request from '@/utils/request'
export function geiListPage (parameter) {
  console.log(parameter);
  return request({
    url: '/system/organize/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/system/organize/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  console.log(ids);
  return request({
    url: '/system/organize/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/organize/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function getTreeList (parameter) {
  return request({
    url: '/system/organize/getTreeList',
    method: 'post',
    data: parameter
  })
}


export function getRoleAuth (parameter) {
  return request({
    url: '/system/organize/findRoleAuth',
    method: 'post',
    data: parameter
  })
}


export function updateAuth (parameter) {
  return request({
    url: '/system/organize/updateAuth',
    method: 'post',
    data: parameter
  })
}

