const axios = require('axios')

let runner = async _ => {
  // average value of array
  const avg = arr => arr.reduce((acc,v,i,a)=>(acc+v/a.length),0)
  // random number between MIN / MAX
  const random = (min, max) => Math.floor(Math.random() * (max - min)) + min

  const getCallSessionByKey = key => {
    const start = Date.now()
    return axios.get(`http://localhost:9091/api/v1/find-call-session?key=${key}`).then((response) => {
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

  let responseDataAndTime = await getCallSessionByKey(key)
  console.log(responseDataAndTime)

  await axios.post('http://localhost:9091/api/v1/save-call-session', newSession).catch(_ => { 
    console.log(`Problem saving call session with key: ${key}. Program terminated.`)
    return
  })
  console.log(`Saved call session with key: ${key}`)

  responseDataAndTime = await getCallSessionByKey(key)
  console.log(responseDataAndTime)

  await axios.delete(`http://localhost:9091/api/v1/delete-call-session?key=${key}`).catch(_ => { 
    console.log(`Problem deleting call session with key: ${key}. Program terminated.`)
    return
  })
  console.log(`Deleted call session with key: ${key}`)

  responseDataAndTime = await getCallSessionByKey(key)
  console.log(responseDataAndTime)
}

runner()
