// frontend/src/app/core/services/geolocation.service.ts
import { Injectable } from '@angular/core';
import { Observable, Observer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GeolocationService {

  getCurrentPosition(): Observable<{ lat: number; lng: number; cidade: string; estado: string }> {
    return new Observable((observer: Observer<{ lat: number; lng: number; cidade: string; estado: string }>) => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          async (position) => {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;

            // Simular reverse geocoding (sem API Key)
            // Em produção, use a API do Google Maps
            const location = await this.reverseGeocodeSimulado(lat, lng);

            observer.next({
              lat,
              lng,
              cidade: location.cidade,
              estado: location.estado
            });
            observer.complete();
          },
          (error) => {
            console.error('Erro ao obter localização:', error);
            observer.error(error);
          }
        );
      } else {
        observer.error(new Error('Geolocalização não suportada'));
      }
    });
  }

  private async reverseGeocodeSimulado(lat: number, lng: number): Promise<{ cidade: string; estado: string }> {
    // Simulação - em produção usar API do Google Maps
    // Baseado nas coordenadas, retornar cidade/estado aproximados
    if (lat > -23.7 && lat < -23.4 && lng > -46.8 && lng < -46.4) {
      return { cidade: 'São Paulo', estado: 'SP' };
    }
    if (lat > -22.9 && lat < -22.7 && lng > -43.3 && lng < -43.1) {
      return { cidade: 'Rio de Janeiro', estado: 'RJ' };
    }
    if (lat > -19.9 && lat < -19.8 && lng > -43.9 && lng < -43.8) {
      return { cidade: 'Belo Horizonte', estado: 'MG' };
    }
    if (lat > -25.4 && lat < -25.3 && lng > -49.3 && lng < -49.2) {
      return { cidade: 'Curitiba', estado: 'PR' };
    }

    return { cidade: 'Não identificada', estado: 'XX' };
  }
}
