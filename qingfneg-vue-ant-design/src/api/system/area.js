import request from '@/utils/request'
export function geiListPage (parameter) {
  console.log(parameter);
  return request({
    url: '/system/area/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/system/area/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  console.log(ids);
  return request({
    url: '/system/area/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/area/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function getTreeList (parameter) {
  return request({
    url: '/system/area/getTreeList',
    method: 'post',
    data: parameter
  })
}




