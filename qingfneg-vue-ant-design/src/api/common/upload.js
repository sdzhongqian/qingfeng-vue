import request from '@/utils/request'
export function upload (formData) {
  return request({
    url: '/common/upload/uploadFile',
    method: 'post',
    data: formData
  })
}
