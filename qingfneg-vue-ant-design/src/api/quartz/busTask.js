import request from '@/utils/request'
export function getListPage (parameter) {
  return request({
    url: '/quartz/busTask/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  return request({
    url: '/quartz/busTask/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function del (ids) {
  return request({
    url: '/quartz/busTask/del?param_ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function stopOrRestore(parameter) {
  return request({
    url: '/quartz/busTask/stopOrRestore',
    method: 'post',
    data: parameter
  })
}

export function execution(jobName,jobGroup) {
  return request({
    url: '/quartz/busTask/execution?jobName='+jobName+'&jobGroup='+jobGroup,
    method: 'get'
  })
}


  