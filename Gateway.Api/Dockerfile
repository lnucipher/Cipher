FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app

EXPOSE 5000

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Development
WORKDIR /src

COPY ["Gateway.Api/Gateway.Api.csproj", "Gateway.Api/"]

RUN dotnet restore "Gateway.Api/Gateway.Api.csproj"

COPY . .
WORKDIR "/src/Gateway.Api"
RUN dotnet build "Gateway.Api.csproj" -c $BUILD_CONFIGURATION -o /app/build

FROM build AS publish
ARG BUILD_CONFIGURATION=Development
RUN dotnet publish "Gateway.Api.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .

ENTRYPOINT ["dotnet", "Gateway.Api.dll"]
