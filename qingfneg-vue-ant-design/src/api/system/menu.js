import request from '@/utils/request'
export function geiListPage (parameter) {
  return request({
    url: '/system/menu/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  return request({
    url: '/system/menu/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  return request({
    url: '/system/menu/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/menu/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function getTreeList (parameter) {
  return request({
    url: '/system/menu/findList',
    method: 'post',
    data: parameter
  })
}




