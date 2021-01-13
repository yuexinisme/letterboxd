FROM openjdk:12-jdk-alpine



# Copy the current directory contents into the container at /app
COPY target/demo.jar /demo.jar

# Getting the updates for Ubuntu and installing python into our environment

# Run app.py when the container launches
ENTRYPOINT ["java","-jar","/demo.jar"]