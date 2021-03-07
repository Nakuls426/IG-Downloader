# IG-Downloader
<p align="center">
<img  width="200" align="center" src="https://cdn1.iconfinder.com/data/icons/social-media-outline-6/128/SocialMedia_Instagram-Outline-512.png">
</p>

An app which allows you to download instagram profile pictures and videos in HD quality **Without login required.**

### Features
* Automatically detects a video.
* IGTV, reels, and slide videos.
* Download profile picture in full size with high quality.

### Some Screenshots from the App
<img src = "https://i.postimg.cc/44zDXFyZ/Screenshot-2021-01-11-20-13-35-458-com-ig-igdownloader.jpg" width="250"/> <img src = "https://i.postimg.cc/5y1kHbyJ/Screenshot-2021-01-11-20-14-24-274-com-ig-igdownloader.jpg" width="250"/> <img src = "https://i.postimg.cc/wj34RWq1/Screenshot-2021-01-11-20-15-59-234-com-ig-igdownloader.jpg" width="250"/>

#### API Usage

##### This endpoint will return username and userid.
```sh
https://instagram-unofficial-api.herokuapp.com/unofficial/api/user?link={profilelink}
```
##### Example : https://instagram-unofficial-api.herokuapp.com/unofficial/api/user?link=https://www.instagram.com/instagram/

#### *JSON result* :
```json 
{
 id: "25025320",
 username: "instagram"
}
```
##### This endpoint will return basic info of the profile and full high quality profile pictures.
```sh
https://instagram-unofficial-api.herokuapp.com/unofficial/api/profile?userid={userid}
```
##### Example : https://instagram-unofficial-api.herokuapp.com/unofficial/api/profile?userid=25025320

#### *JSON result* :
```json {
user: {
user_id: 25025320,
username: "instagram",
full_name: "Instagram",
follower_count: 387949269,
following_count: 54,
biography: "Bringing you closer to the people and things you love. ❤️",
external_url: "https://help.instagram.com/",
profile_pic_url_small: "https://scontent-iad3-1.cdninstagram.com/v/t51.2885-19/s150x150/119381356_363756831450146_3008355575418576013_n.jpg?tp=1&_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_ohc=dqNoZVSP4FsAX9v-2yR&oh=e4761670957193e3e0658c3552046475&oe=606D1FF6",
hd_profile_pic_url_info: {
width: 1080,
url: "https://scontent-iad3-1.cdninstagram.com/v/t51.2885-19/119381356_363756831450146_3008355575418576013_n.jpg?_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_ohc=dqNoZVSP4FsAX9v-2yR&oh=193c0145b1f5d164d97a8d0eb7ee2f26&oe=606E978E",
height: 1080
}
},
hd_profile_pic_versions: [
{
width: 320,
url: "https://scontent-iad3-1.cdninstagram.com/v/t51.2885-19/s320x320/119381356_363756831450146_3008355575418576013_n.jpg?tp=1&_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_ohc=dqNoZVSP4FsAX9v-2yR&oh=a8fef1d95c9430c984707e01dc827692&oe=606DF68E",
height: 320
},
{
width: 640,
url: "https://scontent-iad3-1.cdninstagram.com/v/t51.2885-19/s640x640/119381356_363756831450146_3008355575418576013_n.jpg?tp=1&_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_ohc=dqNoZVSP4FsAX9v-2yR&oh=4e284e3c818eda58edb807bc5957843d&oe=60701337",
height: 640
}
],
status: "ok"
}
```

#### This endpoint will return videos.
```sh
https://instagram-unofficial-api.herokuapp.com/unofficial/api/video?link={link}
```
##### Example : https://instagram-unofficial-api.herokuapp.com/unofficial/api/video?link=https://www.instagram.com/p/CMDbyP7DTtF/

#### *JSON result* :
``` json
{
no_of_videos: 10,
info: [
{
shortcode: "CHFJeEFpmyi",
video_url: "https://scontent-iad3-1.cdninstagram.com/v/t50.2886-16/123601353_723747584893741_745250004650353999_n.mp4?efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtLmRlZmF1bHQiLCJxZV9ncm91cHMiOiJbXCJpZ193ZWJfZGVsaXZlcnlfdnRzX290ZlwiXSJ9&_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_cat=101&_nc_ohc=_brOpQEl-z4AX-sImSh&vs=17884102927802277_4250235783&_nc_vs=HBksFQAYJEdNa0JYZ2N0WTdTZFBwSUNBRS1sQ2tUdXFGY0tia1lMQUFBRhUAAsgBABUAGCRHUEdFWEFkanFCNF9BUjREQURGVE1jX2xzTVFfYmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMBUAACbKpqmL5t%2FEPxUCKAJDMywXQBN2yLQ5WBAYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&oe=6047635F&oh=828a6ac6145966da38d85a06c81140fd",
thumbnail: "https://scontent-iad3-1.cdninstagram.com/v/t51.2885-15/e35/123431188_770278860272937_6023676883947642385_n.jpg?tp=1&_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_cat=109&_nc_ohc=uxXTADWPIfwAX9y55Hi&oh=d1db8b15c6a6174d172ce17425245e7d&oe=60471F3B"
 ...
}]
}
```

#### This endpoint will return search results by Name.
```sh
https://instagram-unofficial-api.herokuapp.com/unofficial/api/topsearch?query={username}
```
##### Example : https://instagram-unofficial-api.herokuapp.com/unofficial/api/topsearch?query=instagram

#### *JSON result* :
``` json
{
users: [
{
position: 0,
user: {
is_private: false,
account_badges: [ ],
full_name: "Instagram",
profile_pic_id: "2399220043858742892_25025320",
mutual_followers_count: 4,
pk: "25025320",
latest_reel_media: 0,
has_anonymous_profile_picture: false,
profile_pic_url: "https://scontent-iad3-1.cdninstagram.com/v/t51.2885-19/s150x150/119381356_363756831450146_3008355575418576013_n.jpg?tp=1&_nc_ht=scontent-iad3-1.cdninstagram.com&_nc_ohc=dqNoZVSP4FsAX9v-2yR&oh=e4761670957193e3e0658c3552046475&oe=606D1FF6",
is_verified: true,
username: "instagram",
friendship_status: {
is_private: false,
incoming_request: false,
is_restricted: false,
following: false,
outgoing_request: false,
is_bestie: false
}
}
},...
]
}
```
**Libraries Used**
* [Volley](https://github.com/google/volley)
* [CircleImageView](https://github.com/hdodenhof/CircleImageView)
* [Glide](https://github.com/bumptech/glide)


### Made by ❤️ Nakul
