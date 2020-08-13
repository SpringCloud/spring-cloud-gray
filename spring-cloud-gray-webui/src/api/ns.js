import request from '@/utils/request'

export function getDefaultNamespace() {
  return request({
    url: '/namespace/default',
    method: 'get'
  })
}
