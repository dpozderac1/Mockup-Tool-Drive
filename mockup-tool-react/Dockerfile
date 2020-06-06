FROM node:latest as builder
WORKDIR /
COPY package.json yarn.lock ./
RUN yarn
COPY . .
RUN yarn build

FROM nginx:stable-alpine
COPY /nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /build /usr/share/nginx/html

