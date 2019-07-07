import request from '@/utils/request'

export function fetchList(query) {
  var tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  tempData.size = query.limit
  delete tempData['limit']
  var uri = '/gray/track/page'
  if (tempData.serviceId) {
    uri = '/gray/track/pageByService'
  }
  return request({
    url: uri,
    method: 'get',
    params: tempData
  })
}

export function createTrackInfo(data) {
  return request({
    url: '/gray/track/',
    method: 'post',
    data
  })
}

export function updateTrackInfo(data) {
  return request({
    url: '/gray/track/',
    method: 'post',
    data
  })
}

export function deleteTrackInfo(id) {
  return request({
    url: '/gray/track/' + id,
    method: 'delete'
  })
}
