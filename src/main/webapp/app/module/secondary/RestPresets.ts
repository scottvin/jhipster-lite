import { AxiosResponse } from 'axios';
import { mapToPreset, RestPreset } from './RestPreset';
import { Presets } from '@/module/domain/Presets';

export interface RestPresets {
  presets: RestPreset[];
}

export const mapToPresets = (response: AxiosResponse<RestPresets>): Presets => {
  const data = response.data;
  return {
    presets: data.presets.map(mapToPreset),
  };
};
