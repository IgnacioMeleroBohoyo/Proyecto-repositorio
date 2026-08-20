FROM node:18 AS build
# Dockerfile para frontend: compila la SPA con Node y la sirve con nginx
WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
RUN npm run build --if-present

FROM nginx:stable-alpine
COPY --from=build /app/dist/demo-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
