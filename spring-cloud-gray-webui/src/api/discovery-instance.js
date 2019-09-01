import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  delete tempData['limit']
  return request({
    url: '/gray/discover/instances',
    method: 'get',
    params: tempData
  })
}

export function createInstance(data) {
  return request({
    url: '/gray/instance/',
    method: 'post',
    data
  })
}

export function tryChangeInstanceStatus(row, status) {
  const data = { 'serviceId': row.serviceId, 'instanceId': row.instanceId, 'instanceStatus': status }
  return request({
    url: '/gray/discover/instanceInfo/setInstanceStatus',
    method: 'put',
    data
  })
}
