import request from '@/utils/request'
export function getListPage (parameter) {
  console.log(parameter);
  return request({
    url: '/system/role/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/system/role/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function del (ids) {
  console.log(ids);
  return request({
    url: '/system/role/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/role/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function updateAuth (parameter) {
  return request({
    url: '/system/role/updateAuth',
    method: 'post',
    data: parameter
  })
}


export function findRoleMenuList (parameter) {
  return request({
    url: '/system/role/findRoleMenuList',
    method: 'post',
    data: parameter
  })
}



  
  export function getServiceList (parameter) {
    return request({
      url: "",
      method: 'get',
      params: parameter
    })
  }

  