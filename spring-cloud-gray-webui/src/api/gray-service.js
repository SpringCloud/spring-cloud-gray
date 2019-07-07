import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  return request({
    url: '/gray/service/page',
    method: 'get',
    params: tempData
  })
}

export function createService(data) {
  return request({
    url: '/gray/service/',
    method: 'post',
    data
  })
}

export function updateService(data) {
  return request({
    url: '/gray/service/',
    method: 'post',
    data
  })
}

export function deleteService(serviceId) {
  return request({
    url: '/gray/service/' + serviceId,
    method: 'delete'
  })
}
