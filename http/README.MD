### 交叉编译


ndk 交叉编译失败 , `http.ListenAndServe(":7070", http.FileServer(http.Dir('.')))` 编译出来无法运行.

故自己实现

```
CGO_ENABLED=0 GOARM=5 GOOS=linux GOARCH=arm  go build -v  -a -ldflags "-s -w" microhttp.go
```

go version go1.8.3 darwin/amd64 编译
