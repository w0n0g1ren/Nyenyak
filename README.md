# Nyenyak

Nyenyak is a mobile-based application for diagnosing sleeping sickness that runs on the Android operating system

## About

Nyenyak is a mobile-based application for diagnosing sleeping sickness that runs on the Android operating system. It uses a Kotlin programming base with tools using Android Studio in its development. For data, this application uses an API as a link between cloud-based databases.

## Feature

- Login and Logout
- Register
- Diagnose for Sleep Disorder
- Showing all and Latest Diagnose
- Showing artcile talk about sleep disorder

## Screenshoot
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/a005c22f-94d9-42b1-9bdb-6a7907483ba2" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/0e935445-b0be-43ab-a0f8-000e60e01c51" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/0cf42486-28f8-412e-a20b-92f5894174ee" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/f0ddefce-e804-4250-b6c0-bb9f0c3ea5cb" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/0abf5285-9edf-42b6-a330-0fbb103ceb98" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/cdf01033-1874-4f79-9090-930f60571da6" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/c9ec0716-7fcb-478c-80a7-0ee96e3a807b" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/170d89de-6132-4c4d-acf2-f7de59e4962b" width = 20% height = 20%>
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/54fd2562-baff-42a9-8b0a-19bc6d0b4c8e" width = 20% height = 20%>

## Usage
1. Use Android Studio Application version 2022.3.1 Patch 1 (Giraffe) or later
2. Clone this repository and import into Android Studio
   ```
   https://github.com/w0n0g1ren/Nyenyak.git
   ```

## Configuration
1. Edit the base URL section in the ApiConfig File in the Retrofit variable and adjust it to your URL
   ```
        val retrofit = Retrofit.Builder()
            .baseUrl("your UrL")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
   ```

2. Edit the Header section if using a header such as a token in the ApiConfig File in the RequestHeader variable and adjust it to your token configuration
   ```
       val requestHeaders = req.newBuilder()
           .addHeader("Your Key", "Your Token")
           .build()
   ```

## DataFlow
<img src = "https://github.com/w0n0g1ren/Nyenyak/assets/98150123/37e2beb8-d250-4db3-b184-f645efa9c8eb" width = 80% height = 80%>

## Several Link
1. Design Figma
```
https://www.figma.com/file/JLuPwrG9gv1n779KvHV0xm/Nyenyak?type=design&t=0zL5ijWMhX2v8MMH-6
```

2. Demo for Application
```
https://drive.google.com/drive/folders/1ZME_A5c-VCF1XBA-7c7fsFJNEHQtMH0M?usp=drive_link
```
