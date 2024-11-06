# Faza izgradnje aplikacije
FROM node:16 AS build

# Postavi radni direktorijum
WORKDIR /app

# Kopiraj package fajlove i instaliraj zavisnosti
COPY package.json package-lock.json ./
RUN npm install

# Kopiraj ostatak aplikacije i izgradi
COPY . .
RUN npm run build --prod

# Faza serviranja aplikacije pomoću Nginx-a
FROM nginx:alpine

# Kopiraj build output iz prethodne faze u Nginx-ov folder za statički sadržaj
COPY --from=build /app/dist/reservation-app /usr/share/nginx/html

# Izloži port 80 (pošto Nginx sluša na ovom portu)
EXPOSE 80

# Opcionalno: Kopiraj prilagođeni Nginx konfiguracioni fajl (ako ga imaš)
# COPY nginx.conf /etc/nginx/nginx.conf

# Pokreni Nginx u prednjem planu
CMD ["nginx", "-g", "daemon off;"]