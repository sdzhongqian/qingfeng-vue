import request from '@/utils/request'
export function getListPage (parameter) {
  console.log(parameter);
  return request({
    url: '/system/group/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/system/group/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function del (ids) {
  console.log(ids);
  return request({
    url: '/system/group/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/group/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

  