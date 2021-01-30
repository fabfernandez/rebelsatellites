# Rebel Satellites API

An API service designed to triangulate positions and recover messages.

By Fabrizio Fernandez.

## Installation

Using Maven [(Download here)](https://maven.apache.org/):

First, clone this repo.

Then open a command line on the root directory (where the **pom.xml** is located) and execute:

```bash
mvn install
```

This will build the code, run all tests and compile an executable .jar file which you can find in the **/target**
directory.

We can use this .jar file to deploy the app.

## Using the API

**You don't need to install the app in order to use it.**

It's already running in
this [URL](http://rebelsatellites-env.eba-eiqmvrkp.us-east-2.elasticbeanstalk.com/): http://rebelsatellites-env.eba-eiqmvrkp.us-east-2.elasticbeanstalk.com/

___________________________________________________

**Available endpoints:**

## POST /topsecret/

Post here the 3 satellites with the following body structure:

```
{
    "satellites": [
        {
            "name": "kenobi",
            "distance": 100.0,
            "message": [
                "this",
                "",
                "",
                "",
                "message"
            ]
        },
        {
            "name": "skywalker",
            "distance": 115.5,
            "message": [
                "",
                "is",
                "",
                "secret",
                ""
            ]
        },
        {
            "name": "sato",
            "distance": 142.7,
            "message": [
                "",
                "",
                "a",
                "",
                ""
            ]
        }
    ]
}
```

The 200 OK response will have the following format:

```
{
    "position": {
        "x": -487.5,
        "y": 1575.0
    },
    "message": "this is a secret message"
}
```

## POST /topsecret_split/:satellite_name

Post here one satellite with the following body. Keep that in mind :satellite_name is a **path variable.**

If you send two requests with the same :satellite_name, the last one will persist.

```
{
    "distance": 100.0,
    "message": [
        "this",
        "",
        "",
        "secret",
        ""
    ]
}
```

The 200 OK response will have the following format:

```
OK sato received.
```

Once you posted all 3 satellites, you can perform the GET /topsecret_split/ and get the results.

## GET /topsecret_split/

200 OK response:

```
{
    "position": {
        "x": -487.5,
        "y": 1575.0
    },
    "message": "this is a secret message"
}
```

## Errors

Errors calculating positions and messages will return a 404 response with a message explaining the error.

For example, if you try to GET /topsecret_split/ without sending the 3 satellites first:

```
{
    "timestamp": "2021-01-30T21:08:31.233+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Not enough satellites recieved. Minimum is 3.",
    "path": "/topsecret_split/"
}
```

## Limitations

1) Message lag is only expected in the beggining of the messages recieved.

For example, this is a complete message with 3 lag spaces in the beggining:

```
"message": [
        "",
        "",
        "",
        "hello",
        "captain",
        "palpitane"
    ]
```

Other types of lag are not supported in this version.
______________________________

2) The only satellite names supported in this version are:

- kenobi
- sato
- skywalker

(This names are case independent in this API.)

Sending other satellite names will result in something like:

```
{
    "timestamp": "2021-01-30T21:29:44.486+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Satellite DARTHVADER not found",
    "path": "/topsecret/"
}
```
