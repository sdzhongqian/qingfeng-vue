import request from '@/utils/request'

export function getData() {
    return request({
      url: '/monitor/server/systemHardware',
      method: 'get',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
      }
    })
  }



