const axios = require('axios')

let runner = async _ => {
  // average value of array
  const avg = arr => arr.reduce((acc,v,i,a)=>(acc+v/a.length),0)
  // random number between MIN / MAX
  const random = (min, max) => Math.floor(Math.random() * (max - min)) + min

  const getCallSessionByKey = (baseUrl, key) => {
    const start = Date.now()
    const url = `${baseUrl}/api/v1/find-call-session?key=${key}`
    console.log(`quering URL ${url}`)
    return axios.get(url).then((response) => {
      const { data: callSessionData } = response
      const finish = Date.now()
      const time = finish - start
      return { callSessionData, time }
    }).catch(_ => { 
      return {
        callSessionData: [], 
        time: -1
      }
    })
  }

  const baseUrl = 'http://localhost:9091'
  const minRandom = 0
  const maxRandom = 100000

  const callSessionId = random(minRandom, maxRandom)
  const key = `${callSessionId}`
  const newSession = {
    key,
    callSession: {
      callSessionId
    },
    timeout: 1
  }

  let responseDataAndTime = await getCallSessionByKey(baseUrl, key)
  console.log(responseDataAndTime)

  await axios.post(`${baseUrl}/api/v1/save-call-session`, newSession).catch(e => {
    console.log(`Problem saving call session with key: ${key}. Program terminated.`)
    const { response } = e
    const { data } = response
    console.log(data)
    process.exit(1)
  })
  console.log(`Saved call session with key: ${key}`)

  responseDataAndTime = await getCallSessionByKey(baseUrl, key)
  console.log(responseDataAndTime)

  await axios.delete(`${baseUrl}/api/v1/delete-call-session?key=${key}`).catch(e => {
    console.log(`Problem deleting call session with key: ${key}. Program terminated.`)
    const { response } = e
    const { data } = response
    console.log(data)
    process.exit(1)
  })
  console.log(`Deleted call session with key: ${key}`)

  responseDataAndTime = await getCallSessionByKey(baseUrl, key)
  console.log(responseDataAndTime)
}

runner()
