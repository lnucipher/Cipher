FROM node:20

ARG ENV=production

WORKDIR /app

COPY package*.json ./

RUN npm install

RUN if [ "$ENV" = "production" ]; then \
      npm run build -- --configuration=production; \
    else \
      npm run build -- --configuration=development; \
    fi

COPY . .

EXPOSE 4200

CMD ["npm", "start"]
