package main

import (
	"net/http"
	"os"
	"path/filepath"
)

var doc string = "/mnt/sdcard/external_sd"

func main() {
	http.HandleFunc("/", routeMatch)
	http.ListenAndServe(":9090", nil)
}

func routeMatch(w http.ResponseWriter, r *http.Request) {
	var files []string
	if r.URL.Path == "/" {
		files = []string{"index.html"}
	} else {
		files = []string{r.URL.Path, filepath.Join(r.URL.Path, "index.html")}
	}
	if !tryFiles(files, w, r) {
		http.NotFound(w, r)
	}
}

func tryFiles(files []string, w http.ResponseWriter, r *http.Request) bool {
	for _, file := range files {
		var realpath string = filepath.Join(doc, file)
		if f, err := os.Stat(realpath); err == nil {
			if f.Mode().IsRegular() {
				http.ServeFile(w, r, realpath)
				return true
			}
		}
	}
	return false
}
