import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/gray/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/gray/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/gray/user/logout',
    method: 'post'
  })
}

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  tempData.size = query.limit
  delete tempData['limit']
  return request({
    url: '/gray/user/page',
    method: 'get',
    params: tempData
  })
}

export function create(data) {
  return request({
    url: '/gray/user/',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/gray/user/',
    method: 'put',
    data
  })
}

export function resetPassword(data) {
  return request({
    url: '/gray/user/resetPassword',
    method: 'put',
    data
  })
}

export function updatePassword(data) {
  return request({
    url: '/gray/user/updatePassword',
    method: 'put',
    data
  })
}

