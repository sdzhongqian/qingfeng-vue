import request from '@/utils/request'
export function getListPage (parameter) {
  return request({
    url: '/quartz/timTask/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  return request({
    url: '/quartz/timTask/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function del (jobName,jobGroup) {
  return request({
    url: '/quartz/timTask/del?jobName='+jobName+'&jobGroup='+jobGroup,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function stopOrRestore(jobName,jobGroup,status) {
  return request({
    url: '/quartz/timTask/stopOrRestore?jobName='+jobName+'&jobGroup='+jobGroup+'&status='+status,
    method: 'get'
  })
}

export function execution(jobName,jobGroup) {
  return request({
    url: '/quartz/timTask/execution?jobName='+jobName+'&jobGroup='+jobGroup,
    method: 'get'
  })
}


  